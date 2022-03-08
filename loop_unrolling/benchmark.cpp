#include "loop_unrolling.h"
#include <benchmark/benchmark.h>
#include <iostream>

#include <vector>
#include <random>

using namespace std;

static vector<int> generateRandomInts(int size) {

    std::random_device rd;
    std::mt19937 mt(rd());
    std::uniform_real_distribution<double> dist(1, 100);

    vector<int> ints;
    ints.reserve(size);

    for (int i = 0; i < size; i++) {
        ints.push_back(dist(mt));
    }

    return ints;
}

static void benchmarkSimpleLoop(benchmark::State& state) {
    for (auto _ : state) {
        int size = state.range(0);
        auto zippedVector = LoopUnrolling::SimpleLoop(generateRandomInts(size), generateRandomInts(size), size);
        benchmark::DoNotOptimize(zippedVector);
    }
}

static void benchmarkSimpleLoopUnrolled(benchmark::State& state) {
    for (auto _ : state) {
        for (auto i = 0; i < state.range(0); i++) {
            int size = state.range(0);
            auto zippedVector = LoopUnrolling::SimpleLoopUnrolled(generateRandomInts(size), generateRandomInts(size), size);
            benchmark::DoNotOptimize(zippedVector);
        }
    }
}

BENCHMARK(benchmarkSimpleLoop)->RangeMultiplier(10)->Range(1, 10000);
BENCHMARK(benchmarkSimpleLoopUnrolled)->RangeMultiplier(10)->Range(1, 10000);

BENCHMARK_MAIN();
